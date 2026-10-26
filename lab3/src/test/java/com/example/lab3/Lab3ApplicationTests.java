package com.example.lab3;

import com.example.lab3.library.model.Book;
import com.example.lab3.library.model.BookType;
import com.example.lab3.library.model.Member;
import com.example.lab3.library.model.MemberRole;
import com.example.lab3.library.policy.BorrowDecision;
import com.example.lab3.library.policy.BorrowingPolicy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest(properties = "library.policy=drools")
class Lab3ApplicationTests {

	@Autowired
	private BorrowingPolicy borrowingPolicy;

	@Test
	void contextLoads() {
	}

	@Test
	void droolsRulesDecideEligibilityAndLoanPeriod() {
		Member student = new Member(10L, "Ana", MemberRole.STUDENT);
		Book generalBook = new Book(10L, "General book", "Author");
		Book referenceBook = new Book(11L, "Reference book", "Author", BookType.REFERENCE);
		Book rareBook = new Book(12L, "Rare book", "Author", BookType.RARE);
		Book newRelease = new Book(13L, "New release", "Author", BookType.NEW_RELEASE);

		BorrowDecision regularLoan = borrowingPolicy.evaluate(student, generalBook, 0);
		Assertions.assertTrue(regularLoan.allowed());
		Assertions.assertEquals(14, regularLoan.loanDays());

		Assertions.assertFalse(borrowingPolicy.evaluate(student, referenceBook, 0).allowed());
		Assertions.assertFalse(borrowingPolicy.evaluate(student, rareBook, 0).allowed());
		Assertions.assertFalse(borrowingPolicy.evaluate(student, generalBook, 2).allowed());
		Assertions.assertEquals(7, borrowingPolicy.evaluate(student, newRelease, 0).loanDays());

		Book borrowedBook = new Book(14L, "Borrowed book", "Author");
		LocalDate dueDate = LocalDate.now().plusDays(5);
		borrowedBook.borrowTo(99L, dueDate);
		Assertions.assertEquals(dueDate, borrowedBook.getDueDate());
		Assertions.assertFalse(borrowingPolicy.evaluate(student, borrowedBook, 0).allowed());
		borrowedBook.giveBack();
		Assertions.assertNull(borrowedBook.getDueDate());

		Member premium = new Member(15L, "Elena", MemberRole.PREMIUM);
		Assertions.assertEquals(10, borrowingPolicy.evaluate(premium, rareBook, 0).loanDays());

		Member regular = new Member(16L, "Mihai", MemberRole.REGULAR);
		Assertions.assertEquals(21, borrowingPolicy.evaluate(regular, generalBook, 0).loanDays());
	}
}
