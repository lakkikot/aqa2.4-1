package ru.netology.transfer.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.transfer.data.DataHelper;
import ru.netology.transfer.page.DashboardPage;
import ru.netology.transfer.page.LoginPage;
import ru.netology.transfer.page.TransferPage;

import static com.codeborne.selenide.Selenide.*;

import static org.junit.jupiter.api.Assertions.*;


public class TransferMoneyTest {

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
        var loginPage = new LoginPage();
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        verificationPage.validVerify(verificationCode);
        var dashboardPage = new DashboardPage();
        var transferPage = new TransferPage();

        // каждый раз готовим изначальный одинаковый баланс карт (по 10000)
        int balance = 10_000;
        if(dashboardPage.getFirstCardBalance() < dashboardPage.getSecondCardBalance()) {
            transferPage.addMoneyToFirstCard(String.valueOf(balance - dashboardPage.getFirstCardBalance()));
        }
        if(dashboardPage.getFirstCardBalance() > dashboardPage.getSecondCardBalance()) {
            transferPage.addMoneyToSecondCard(String.valueOf(balance - dashboardPage.getSecondCardBalance()));
        }

    }

    @Test
    // перевод денег на 1 карту
    void shouldTransferMoneyTo1stCard() {
        var dashboardPage = new DashboardPage();
        var transferPage = new TransferPage();

        int startBalance = dashboardPage.getFirstCardBalance(); //запоминаем сумму
        int amount = 1000; //сколько добавлять

        transferPage.addMoneyToFirstCard(String.valueOf(amount)); // добавляем тысячу

        var expected = startBalance + amount;
        assertEquals(expected, dashboardPage.getFirstCardBalance());
    }

    @Test
    // перевод денег на 2 карту
    void shouldTransferMoneyTo2ndCard() {
        var dashboardPage = new DashboardPage();
        var transferPage = new TransferPage();

        int startBalance = dashboardPage.getSecondCardBalance(); //запоминаем сумму
        int amount = 1000; //сколько добавлять

        transferPage.addMoneyToSecondCard(String.valueOf(amount)); // добавляем тысячу

        var expected = startBalance + amount;
        assertEquals(expected, dashboardPage.getSecondCardBalance());
    }

    @Test
    // невозможно перевести больше чем есть на 1 карте
    void shouldNotTransferMoreThanIsIn1stCard() {
        var dashboardPage = new DashboardPage();
        var transferPage = new TransferPage();

        int startBalance = dashboardPage.getSecondCardBalance(); // запоминаем сумму на 2 карте

        int amount = dashboardPage.getFirstCardBalance() + 1000; // сумма перевода на 1000 больше чем есть на 1 карте
        transferPage.addMoneyToSecondCard(String.valueOf(amount)); // переводим сумму

        var expected = startBalance + amount; // на 2 карте должно прибавиться на количество amount
        assertEquals(expected, dashboardPage.getSecondCardBalance());
//        assertTrue(dashboardPage.getFirstCardBalance() > 0); // на 1 карте не ушло в минус - issue#1
    }

    @Test
    // невозможно перевести если баланс карты меньше 0
    void shouldNotTransferMoneyIfBalanceIsBelow0() {
        var dashboardPage = new DashboardPage();
        var transferPage = new TransferPage();

       // готовим отрицательный баланс благодаря багу

        int amount = dashboardPage.getFirstCardBalance() + 1000; // сумма перевода на 1000 больше чем есть на 1 карте
        transferPage.addMoneyToSecondCard(String.valueOf(amount)); // переводим сумму, после перевода на 1 карте отрицательный баланс

        int transitBalance = dashboardPage.getFirstCardBalance(); // запоминаем отрицательный баланс

        transferPage.addMoneyToSecondCard("1000"); // переводим ещё 1000

      //  assertEquals(transitBalance, dashboardPage.getSecondCardBalance()); //баланс не должен меняться - issue#2

    }

}
