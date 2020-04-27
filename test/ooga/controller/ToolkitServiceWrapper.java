package ooga.controller;

import com.sun.javafx.application.ParametersImpl;
import javafx.application.Application;
import javafx.stage.Stage;
import org.testfx.toolkit.ApplicationLauncher;
import org.testfx.toolkit.ApplicationService;
import org.testfx.toolkit.impl.ToolkitServiceImpl;
import org.testfx.util.WaitForAsyncUtils;

import java.util.concurrent.Future;
import java.util.function.Supplier;

public class ToolkitServiceWrapper extends ToolkitServiceImpl {

    private final ApplicationLauncher applicationLauncher;
    private final ApplicationService applicationService;

    public ToolkitServiceWrapper(ApplicationLauncher applicationLauncher, ApplicationService applicationService) {
        super(applicationLauncher, applicationService);
        this.applicationLauncher = applicationLauncher;
        this.applicationService = applicationService;
    }

    public Future<Application> setupApplication(Supplier<Stage> stageSupplier, Object app, String... applicationArgs) {
        return WaitForAsyncUtils.async(() -> {
            Application application = (Application)WaitForAsyncUtils.asyncFx(() -> {
                return this.createApplication(app);
            }).get();
            this.registerApplicationParameters(application, applicationArgs);
            this.applicationService.init(application).get();
            this.applicationService.start(application, (Stage)stageSupplier.get()).get();
            return application;
        });
    }

    private void registerApplicationParameters(Application application, String... applicationArgs) {
        ParametersImpl parameters = new ParametersImpl(applicationArgs);
        ParametersImpl.registerParameters(application, parameters);
    }

    private Application createApplication(Object app) throws Exception {
        return (Application)app;
    }
}
