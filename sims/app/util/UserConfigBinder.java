package util;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

import play.data.binding.Global;
import play.data.binding.TypeBinder;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import controllers.dto.AppUserConfiguration;
import controllers.dto.AppUserConfiguration.UserConfiguration;


/**
 * Binding de JSON a UserAppConfig.
 * 
 * 
 * @author Juan Edi
 * @since Jun 24, 2012
 */
@Global
public class UserConfigBinder implements TypeBinder<AppUserConfiguration>{

    /** @see TypeBinder#bind(String, Annotation[], String, Class, Type) */
    @Override
    public Object bind(String name, Annotation[] annotations, String value,
            Class actualClass, Type genericType) throws Exception {
        Gson gson = new Gson();
        Type type = new TypeToken<List<UserConfiguration>>() {}.getType();
        List<controllers.dto.AppUserConfiguration.UserConfiguration> config = gson.fromJson(value, type);
        AppUserConfiguration appUserConfiguration = new AppUserConfiguration();
        appUserConfiguration.userConfigurations = config;
        return appUserConfiguration;
    }

}
