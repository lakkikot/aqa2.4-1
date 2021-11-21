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
    private DashboardPage dashboardPage;

    @BeforeEach
    void setUp() {
        var loginPage = open("http://localhost:9999", LoginPage.class);
        var authInfo = DataHelper.getAuthInfo();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        dashboardPage = verificationPage.validVerify(verificationCode);

    //     каждый раз готовим изначальный одинаковый баланс карт (по 10000)
//        int balance = 10_000;
//        if(dashboardPage.getFirstCardBalance() < dashboardPage.getSecondCardBalance()) {
//            var transferPage = dashboardPage.toFirstCard();
//            transferPage.addMoneyToFirstCard(String.valueOf(balance - dashboardPage.getFirstCardBalance()));
//        }
//        if(dashboardPage.getFirstCardBalance() > dashboardPage.getSecondCardBalance()) {
//            var transferPage = dashboardPage.toSecondCard();
//            dashboardPage.toSecondCard().addMoneyToSecondCard(String.valueOf(balance - dashboardPage.getSecondCardBalance()));
//        }

    }

    @Test
    // перевод денег на 1 карту
    void shouldTransferMoneyTo1stCard() {
        int startBalance1 = dashboardPage.getFirstCardBalance(); //запоминаем сумму 1 карты
        int startBalance2 = dashboardPage.getSecondCardBalance(); //запоминаем сумму 2 карты
        int amount = 1000; //сколько добавлять

        var transferPage = dashboardPage.toFirstCard();
        transferPage.addMoneyToFirstCard(String.valueOf(amount)); // добавляем тысячу

        var expected1 = startBalance1 + amount;
        var expected2 = startBalance2 - amount;
        assertEquals(expected1, dashboardPage.getFirstCardBalance()); //1 карта
        assertEquals(expected2, dashboardPage.getSecondCardBalance()); //2 карта
    }

    @Test
    // перевод денег на 2 карту
    void shouldTransferMoneyTo2ndCard() {

        int startBalance1 = dashboardPage.getFirstCardBalance(); //запоминаем сумму 1 карты
        int startBalance2 = dashboardPage.getSecondCardBalance(); //запоминаем сумму 2 карты
        int amount = 1000; //сколько добавлять

        var transferPage = dashboardPage.toSecondCard();
        transferPage.addMoneyToSecondCard(String.valueOf(amount)); // добавляем тысячу

        var expected1 = startBalance1 - amount;
        var expected2 = startBalance2 + amount;
        assertEquals(expected1, dashboardPage.getFirstCardBalance()); //1 карта
        assertEquals(expected2, dashboardPage.getSecondCardBalance()); //2 карта
    }

    @Test
    // невозможно перевести больше чем есть на 1 карте
    void shouldNotTransferMoreThanIsIn1stCard() {

        int startBalance1 = dashboardPage.getFirstCardBalance(); //запоминаем сумму 1 карты
        int startBalance2 = dashboardPage.getSecondCardBalance(); //запоминаем сумму 2 карты

        int amount = dashboardPage.getFirstCardBalance() + 1000; // сумма перевода на 1000 больше чем есть на 1 карте

        var transferPage = dashboardPage.toSecondCard();
        transferPage.addMoneyToSecondCard(String.valueOf(amount)); // переводим сумму

        var expected1 = startBalance1; // баланс не меняется
        var expected2 = startBalance2;

        assertEquals(expected1, dashboardPage.getFirstCardBalance()); //1 карта
        assertEquals(expected2, dashboardPage.getSecondCardBalance());
                assertTrue(dashboardPage.getFirstCardBalance() > 0); // на 1 карте не ушло в минус - issue#1
    }

    @Test
    // невозможно перевести если баланс карты меньше 0
    void shouldNotTransferMoneyIfBalanceIsBelow0() {

       // готовим отрицательный баланс благодаря багу

        int amount = dashboardPage.getFirstCardBalance() + 1000; // сумма перевода на 1000 больше чем есть на 1 карте1

        var transferPage = dashboardPage.toSecondCard();
        transferPage.addMoneyToSecondCard(String.valueOf(amount)); // переводим сумму, после перевода на 1 карте отрицательный баланс

        int transitBalance = new DashboardPage().getFirstCardBalance(); // запоминаем отрицательный баланс
        transferPage = dashboardPage.toSecondCard();
        transferPage.addMoneyToSecondCard("1000"); // переводим ещё 1000

        assertEquals(transitBalance, dashboardPage.getFirstCardBalance()); //баланс не должен меняться - issue#2

    }

}
