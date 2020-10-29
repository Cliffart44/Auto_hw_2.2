import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class PageUiTest {

    @Test
    void shouldSubmitRequest() {
        open("http://localhost:9999/");
        $("[data-test-id=city] .input__control").setValue("Москва");
        $("[data-test-id=date] [placeholder=\"Дата встречи\"]").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE, LocalDate.now().plusDays(7).format(DateTimeFormatter.ofPattern("dd.MM.uuuu")));
        $("[data-test-id=name] [name=name]").setValue("Альберт Эйнштейн");
        $("[data-test-id=phone] [name=phone]").setValue("+14318791955");
        $("[data-test-id=agreement]>.checkbox__box").click();
        $("button>.button__content").click();
        $("[data-test-id=notification]").waitUntil(visible, 15000).shouldHave(exactText("Успешно! Встреча успешно забронирована на " + LocalDate.now().plusDays(7).format(DateTimeFormatter.ofPattern("dd.MM.uuuu"))));
    }

    @Test
    void shouldSubmitComplexRequest() {
        long daysToAdd = 7;
        int year = Integer.parseInt(LocalDate.now().format(DateTimeFormatter.ofPattern("uuuu")));
        int month = Integer.parseInt(LocalDate.now().format(DateTimeFormatter.ofPattern("M")));
        int day = Integer.parseInt(LocalDate.now().format(DateTimeFormatter.ofPattern("d")));
        int daysToMonthsEnd = LocalDate.of(year, month, day).getMonth().length(LocalDate.of(year, month, day).lengthOfYear() > 365) - day;
        int monthOffset = Integer.parseInt(LocalDate.now().plusDays(daysToAdd).format(DateTimeFormatter.ofPattern("MM"))) - month;
//        int yearOffset = Integer.parseInt(LocalDate.now().plusDays(daysToAdd).format(DateTimeFormatter.ofPattern("uuuu"))) - year;
        open("http://localhost:9999/");
        $("[data-test-id=city] .input__control").setValue("Мо");
        $$(".menu-item__control").findBy(text("Москва")).click();
        $(".icon-button__text>.icon_name_calendar").click();
//        if (yearOffset > 1) {
//            for (int i = 0; i < yearOffset; i++) {
//                $("[data-step=\"12\"]").click();
//            }
        if (daysToMonthsEnd > 2) {
            for (int i = 0; i < monthOffset; i++) {
                $("[data-step=\"1\"]").click();
            }
        } else
            for (int i = 1; i < monthOffset; i++) {
                $("[data-step=\"1\"]").click();
            }
        $$(".calendar__day").findBy(text(LocalDate.now().plusDays(daysToAdd).format(DateTimeFormatter.ofPattern("d")))).click();
        $("[data-test-id=name] [name=name]").setValue("Альберт Эйнштейн");
        $("[data-test-id=phone] [name=phone]").setValue("+14318791955");
        $("[data-test-id=agreement]>.checkbox__box").click();
        $("button>.button__content").click();
        $("[data-test-id=notification]").waitUntil(visible, 15000).shouldHave(exactText("Успешно! Встреча успешно забронирована на " + LocalDate.now().plusDays(daysToAdd).format(DateTimeFormatter.ofPattern("dd.MM.uuuu"))));
    }
}