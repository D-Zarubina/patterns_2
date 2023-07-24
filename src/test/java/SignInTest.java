import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.*;

public class SignInTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful login if status active")
    void shouldSuccessfulLoginIfStatusActive() {
        var registeredUser = DataGenerator.Registration.getRegisteredUser("active");
        $("[name='login']").setValue(registeredUser.getLogin());
        $("[name='password']").setValue(registeredUser.getPassword());
        $$("[class='button__text']").find(Condition.exactText("Продолжить")).click();
        $("h2").should(Condition.text("Личный кабинет")).should(Condition.visible);
    }

    @Test
    @DisplayName("Error message if user is blocked")
    void errorMessageIfUserIsBlocked() {
        var registeredUser = DataGenerator.Registration.getRegisteredUser("blocked");
        $("[name='login']").setValue(registeredUser.getLogin());
        $("[name='password']").setValue(registeredUser.getPassword());
        $$("[class='button__text']").find(Condition.exactText("Продолжить")).click();
        $("div.notification__content").should(Condition.text("Пользователь заблокирован"))
                .should(Condition.visible);
    }

    @Test
    @DisplayName("Error message if user is not registered")
    void errorMessageIfUserIsNotRegistered() {
        var notRegisteredUser = DataGenerator.Registration.getUser("active");
        $("[name='login']").setValue(notRegisteredUser.getLogin());
        $("[name='password']").setValue(notRegisteredUser.getPassword());
        $$("[class='button__text']").find(Condition.exactText("Продолжить")).click();
        $("div.notification__content").should(Condition.text("Неверно указан логин или пароль"))
                .should(Condition.visible);
    }

    @Test
    @DisplayName("Error message if login with wrong password")
    void errorMessageIfWrongPassword() {
        var registeredUser = DataGenerator.Registration.getRegisteredUser("active");
        var wrongPassword = DataGenerator.getRandomPassword();
        $("[name='login']").setValue(registeredUser.getLogin());
        $("[name='password']").setValue(wrongPassword);
        $$("[class='button__text']").findBy(Condition.exactText("Продолжить")).click();
        $("div.notification__content").should(Condition.text("Неверно указан логин или пароль"))
                .should(Condition.visible);

    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = DataGenerator.Registration.getRegisteredUser("active");
        var wrongLogin = DataGenerator.getRandomLogin();
        $("[name='login']").setValue(wrongLogin);
        $("[name='password']").setValue(registeredUser.getPassword());
        $$("[class='button__text']").find(Condition.exactText("Продолжить")).click();
        $("div.notification__content").should(Condition.text("Неверно указан логин или пароль"))
                .should(Condition.visible);
    }
}
