package com.Library.controller;

import com.Library.entity.Book;
import com.Library.entity.BookTransaction;
import com.Library.service.BookService;
import com.Library.service.BookTransactionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.Optional;

@Controller
public class BookController {

    private final BookService bookService;
    private final BookTransactionService bookTransactionService;

    public BookController(BookService bookService, BookTransactionService bookTransactionService) {
        this.bookService = bookService;
        this.bookTransactionService = bookTransactionService;
    }

    // Home Page
    @GetMapping({"/", "/home"})
    public String homePage() {
        return "home";
    }

    // Books list root
    @GetMapping("/books")
    public String booksRootRedirect() {
        return "redirect:/books/list";
    }

    // Add Book Page
    @GetMapping("/books/add")
    public String showAddForm(Model model) {
        if (!model.containsAttribute("book")) {
            model.addAttribute("book", new Book());
        }
        return "addbook";
    }

    // Add Book Submit
    @PostMapping("/books/add")
    public String addBook(@ModelAttribute("book") Book book) {
        bookService.saveBook(book);
        return "redirect:/books/list";
    }

    // Show All Books
    @GetMapping("/books/list")
    public String listBooks(Model model) {
        model.addAttribute("books", bookService.getAllBooks());
        return "booklist";
    }

    // Show Edit Form
    @GetMapping("/books/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Optional<Book> book = bookService.getBookById(id);
        if (book.isPresent()) {
            model.addAttribute("book", book.get());
            return "update";
        }
        return "redirect:/books/list";
    }

    // Update Book
    @PostMapping("/books/update")
    public String updateBook(@ModelAttribute Book book) {
        bookService.saveBook(book);
        return "redirect:/books/list";
    }

    // Delete a Book
    @PostMapping("/books/delete/{id}")
    public String deleteBook(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            bookService.deleteBook(id);
            redirectAttributes.addFlashAttribute("successMessage", "Book deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting book: " + e.getMessage());
        }
        return "redirect:/books/list";
    }

    // Show Issue Form
    @GetMapping("/books/issue/{bookId}")
    public String showIssueForm(@PathVariable("bookId") Long bookId, Model model) {
        Optional<Book> bookOpt = bookService.getBookById(bookId);
        if (bookOpt.isEmpty()) {
            return "redirect:/books/list";
        }

        BookTransaction transaction = new BookTransaction();
        transaction.setBook(bookOpt.get());
        transaction.setIssueDate(LocalDate.now());

        model.addAttribute("transaction", transaction);
        model.addAttribute("book", bookOpt.get());

        return "issue";
    }

    // Issue Book
    @PostMapping("/books/issue/submit")
    public String issueBook(@ModelAttribute("transaction") BookTransaction transaction, Model model) {
        Long bookId = transaction.getBook() != null ? transaction.getBook().getId() : null;

        if (bookId == null) {
            model.addAttribute("error", "Invalid book reference.");
            return "issue";
        }

        boolean success = bookTransactionService.issueBook(bookId, transaction.getIssuedTo());

        if (!success) {
            model.addAttribute("error", "Book is not available.");
            model.addAttribute("book", transaction.getBook());
            return "issue";
        }

        return "redirect:/books/list";
    }

    // Transaction History
    @GetMapping("/transactions/history")
    public String showHistory(Model model) {
        model.addAttribute("transactions", bookTransactionService.getAllTransactions());
        return "history";
    }

    // Return Book
    @PostMapping("/returnBook/{id}")
    public String returnBook(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        boolean returned = bookTransactionService.returnBook(id);
        if (returned) {
            redirectAttributes.addFlashAttribute("successMessage", "✅ Book returned successfully.");
        }
        return "redirect:/transactions/history";
    }

    // Delete All History (only if all returned)
    @PostMapping("/deleteAllTransactions")
    public String deleteAllTransactions(RedirectAttributes redirectAttributes) {
        if (bookTransactionService.hasUnreturnedTransactions()) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "❌ Cannot clear history until all issued books are returned!");
            return "redirect:/transactions/history";
        }

        bookTransactionService.deleteAll();
        redirectAttributes.addFlashAttribute("successMessage", "✅ All history deleted successfully!");
        return "redirect:/transactions/history";
    }
}
