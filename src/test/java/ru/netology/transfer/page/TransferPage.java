package ru.netology.transfer.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.netology.transfer.data.DataHelper;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.openqa.selenium.Keys.*;

public class TransferPage {
    ElementsCollection cards = $$(".list__item");

    SelenideElement amountField = $("[data-test-id=amount] input"); // поле для ввода суммы перевода
    SelenideElement cardFrom = $("[data-test-id=from] input"); // с какой карты

    SelenideElement buttonTransfer = $("[data-test-id='action-transfer']"); // кнопка

    public DashboardPage addMoneyToFirstCard(String amount) {

        var card1 = DataHelper.getFirstCardInfo();
        var card2 = DataHelper.getSecondCardInfo();
        cards.findBy(text(card1.getShortNumber())).find("[data-test-id=action-deposit]").click(); // "пополнить" для первой карты
        amountField.sendKeys(CONTROL+"A", DELETE);
        amountField.setValue(amount); // вводим переданное количество
        cardFrom.doubleClick().sendKeys(CONTROL+"A", DELETE);
        cardFrom.setValue(card2.getNumber()); // вводим номер карты2
        buttonTransfer.click();

        return new DashboardPage();
    }

    public DashboardPage addMoneyToSecondCard(String amount) {

        var card1 = DataHelper.getFirstCardInfo();
        var card2 = DataHelper.getSecondCardInfo();
        cards.findBy(text(card2.getShortNumber())).find("[data-test-id=action-deposit]").click(); // "пополнить" для второй карты
        amountField.sendKeys(CONTROL+"A", DELETE);
        amountField.setValue(amount); // вводим переданное количество
        cardFrom.doubleClick().sendKeys(CONTROL+"A", DELETE);
        cardFrom.setValue(card1.getNumber()); // вводим номер карты1
        buttonTransfer.click();

        return new DashboardPage();
    }

}
