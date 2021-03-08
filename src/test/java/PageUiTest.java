import org.junit.jupiter.api.Test;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.Keys;
import org.openqa.selenium.support.Color;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Condition.*;
import static org.junit.jupiter.api.Assertions.*;

public class PageUiTest {
    int delay = 15;

//    @NotNull
//    private String when(boolean trim) {
//        long daysToAdd = 7;
//        if (trim) return LocalDate.now().plusDays(daysToAdd).format(DateTimeFormatter.ofPattern("d"));
//        else return LocalDate.now().plusDays(daysToAdd).format(DateTimeFormatter.ofPattern("dd.MM.uuuu"));
//    }

    @NotNull
    private String when(boolean trim) {
        Calendar cl = new GregorianCalendar();
        cl.add(Calendar.DATE, 7);
        if (trim) {
            return new SimpleDateFormat("d").format(cl.getTime());
        } else {
            return new SimpleDateFormat("dd.MM.yyyy").format(cl.getTime());
        }
    }

    @Test
    void shouldSubmitRequest() {
        open("http://localhost:9999/");
        $("[data-test-id=city] .input__control").setValue("Москва");
        $("[data-test-id=date] [placeholder=\"Дата встречи\"]").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE, when(false));
        $("[data-test-id=name] [name=name]").setValue("Альберт Эйнштейн");
        $("[data-test-id=phone] [name=phone]").setValue("+14318791955");
        $("[data-test-id=agreement]>.checkbox__box").click();
        $("button>.button__content").click();
        $("[data-test-id=notification]").shouldBe(visible, Duration.ofSeconds(delay)).shouldHave(exactText("Успешно! Встреча успешно забронирована на " + when(false)));
    }

    @Test
    void shouldSubmitComplexRequest() {
        open("http://localhost:9999/");
        $("[data-test-id=city] .input__control").setValue("Мо");
        $$(".menu-item__control").findBy(text("Москва")).click();
        $("[data-test-id=date] [placeholder=\"Дата встречи\"]").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE, when(false), Keys.CONTROL + "A", Keys.DELETE);
        $$(".calendar__day").findBy(text(when(true))).click();
        $("[data-test-id=name] [name=name]").setValue("Альберт Эйнштейн");
        $("[data-test-id=phone] [name=phone]").setValue("+14318791955");
        $("[data-test-id=agreement]>.checkbox__box").click();
        $("button>.button__content").click();
        $("[data-test-id=notification]").shouldBe(visible, Duration.ofSeconds(delay)).shouldHave(exactText("Успешно! Встреча успешно забронирована на " + when(false)));
    }

    @Test
    void shouldVerifyCheckboxColour() {
        open("http://localhost:9999/");
        $("[data-test-id=city] .input__control").setValue("Москва");
        $("[data-test-id=date] [placeholder=\"Дата встречи\"]").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE, when(false));
        $("[data-test-id=name] [name=name]").setValue("Альберт Эйнштейн");
        $("[data-test-id=phone] [name=phone]").setValue("+14318791955");
        $("button>.button__content").click();
        String checkboxColour = Color.fromString($(".checkbox__text").getCssValue("color")).asHex();
        assertEquals("#ff5c5c", checkboxColour);
    }
}