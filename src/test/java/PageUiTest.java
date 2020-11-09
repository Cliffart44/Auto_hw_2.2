import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;

public class PageUiTest {
    int daysToAdd = 7;

    String getFormattedDate(int daysToAdd, String patternOfDate) {
        return LocalDate.now().plusDays(daysToAdd).format(DateTimeFormatter.ofPattern(patternOfDate));
    }

    int yearOffset(int daysToAdd) {
        return LocalDate.now().plusDays(daysToAdd).getYear() - LocalDate.now().plusDays(3).getYear();
    }

    int monthOffset(int daysToAdd) {
        return LocalDate.now().plusDays(daysToAdd).getMonthValue() - LocalDate.now().plusDays(3).getMonthValue();
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999/");
    }

    @Test
    void shouldSubmitRequest() {
        $("[data-test-id=city] .input__control").setValue("Москва");
        $("[data-test-id=date] [placeholder=\"Дата встречи\"]").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE, LocalDate.now().plusDays(daysToAdd).format(DateTimeFormatter.ofPattern("dd.MM.uuuu")));
        $("[data-test-id=name] [name=name]").setValue("Альберт Эйнштейн");
        $("[data-test-id=phone] [name=phone]").setValue("+14318791955");
        $("[data-test-id=agreement]>.checkbox__box").click();
        $("button>.button__content").click();
        $("[data-test-id=notification]").waitUntil(visible, 15000).shouldHave(exactText("Успешно! Встреча успешно забронирована на " + LocalDate.now().plusDays(daysToAdd).format(DateTimeFormatter.ofPattern("dd.MM.uuuu"))));
    }

    @Test
    void shouldSubmitComplexRequest() {
        $("[data-test-id=city] .input__control").setValue("Мо");
        $$(".menu-item__control").findBy(text("Москва")).click();
        $(".icon-button__text>.icon_name_calendar").click();
        if (yearOffset(daysToAdd) > 0) {
            for (int i = 0; i < yearOffset(daysToAdd); i++) {
                $(".calendar__arrow_direction_right[data-step='12']").click();
            }
            if (monthOffset(daysToAdd) > 0) {
                for (int i = 0; i < monthOffset(daysToAdd); i++) {
                    $(".calendar__arrow_direction_right[data-step='1']").click();
                }
            } else {
                for (int i = 0; i > monthOffset(daysToAdd); i--) {
                    $(".calendar__arrow_direction_left[data-step='-1']").click();
                }
            }
        } else {
            for (int i = 0; i < monthOffset(daysToAdd); i++) {
                $(".calendar__arrow_direction_right[data-step='1']").click();
            }
        }
        $$(".calendar__day").findBy(text(LocalDate.now().plusDays(daysToAdd).format(DateTimeFormatter.ofPattern("d")))).click();
        $("[data-test-id=name] [name=name]").setValue("Альберт Эйнштейн");
        $("[data-test-id=phone] [name=phone]").setValue("+14318791955");
        $("[data-test-id=agreement]>.checkbox__box").click();
        $("button>.button__content").click();
        $("[data-test-id=notification]").waitUntil(visible, 15000).shouldHave(exactText("Успешно! Встреча успешно забронирована на " + getFormattedDate(daysToAdd, "dd.MM.yyyy")));
    }
}