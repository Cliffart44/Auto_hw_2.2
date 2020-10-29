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
        open("http://localhost:9999/");
        $("[data-test-id=city] .input__control").setValue("Мо");
        $$(".menu-item__control").findBy(text("Москва")).click();
        $("[data-test-id=date] [placeholder=\"Дата встречи\"]").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE, LocalDate.now().plusDays(7).format(DateTimeFormatter.ofPattern("dd.MM.uuuu")), Keys.CONTROL + "A", Keys.DELETE);
//        $$(".calendar__day").findBy(text(LocalDate.now().plusDays(7).format(DateTimeFormatter.ofPattern("d")))).click();
        $("[data-test-id=name] [name=name]").setValue("Альберт Эйнштейн");
        $("[data-test-id=phone] [name=phone]").setValue("+14318791955");
        $("[data-test-id=agreement]>.checkbox__box").click();
        $("button>.button__content").click();
        $("[data-test-id=notification]").waitUntil(visible, 15000).shouldHave(exactText("Успешно! Встреча успешно забронирована на " + LocalDate.now().plusDays(7).format(DateTimeFormatter.ofPattern("dd.MM.uuuu"))));
    }
}