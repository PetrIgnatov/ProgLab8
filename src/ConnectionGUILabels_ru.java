import java.util.ListResourceBundle;

public class ConnectionGUILabels_ru extends ListResourceBundle {
	private Object[][] contents = {{"IP","IP: "}, {"PORT", "ПОРТ: "}, {"LOGIN","ЛОГИН: "},{"PASSWORD","ПАРОЛЬ: "}, {"CONFIRM", "ПОДТВЕРДИТЬ"}, {"REGISTER", "ЗАРЕГИСТРИРОВАТЬСЯ"}, {"SIGN IN", "ВОЙТИ"}, {"SISUCCESS", "ВЫ УСПЕШНО ВОШЛИ!"}, {"SIERROR","ПОПРОБУЙТЕ ДРУГОЙ ЛОГИН ИЛИ ПАРОЛЬ"}, {"REGSUCCESS","ВЫ УСПЕШНО ЗАРЕГИСТРИРОВАЛИСЬ"}, {"REGERROR","ПОПРОБУЙТЕ ДРУГОЙ ЛОГИН ИЛИ ПАРОЛЬ"}, {"CONSUCCESS", "ПОДКЛЮЧЕНИЕ ПРОШЛО УСПЕШНО!"}, {"CONERROR", "НЕ УДАЛОСЬ ПОДКЛЮЧИТЬСЯ! ПОПРОБУЙТЕ ДРУГОЙ IP ИЛИ ПОРТ!"}};
	@Override
	public Object[][] getContents() { return contents; }
}
