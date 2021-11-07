package ru.netology.transfer.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.netology.transfer.data.DataHelper;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class DashboardPage {
    private SelenideElement heading = $("[data-test-id=dashboard]");

 //   private ElementsCollection cards = $$(".list__item");

    private SelenideElement firstCard = $("li:nth-child(1) > div");
    private SelenideElement secondCard = $("li:nth-child(2) > div");

    private final String balanceStart = "баланс: ";
    private final String balanceFinish = " р.";

    public DashboardPage() {
        heading.shouldBe(visible);
    }

    // вычисляет баланс
    private int extractBalance(String text) {
        var start = text.indexOf(balanceStart);
        var finish = text.indexOf(balanceFinish);
        var value = text.substring(start + balanceStart.length(), finish);
        return Integer.parseInt(value);
    }

    public int getFirstCardBalance() {
        var text = firstCard.text();
        return extractBalance(text);
    }

    public int getSecondCardBalance() {
        var text = secondCard.text();
        return extractBalance(text);
    }

}
