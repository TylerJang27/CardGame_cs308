package ooga.controller;

import javafx.application.Application;
import org.testfx.api.FxToolkit;
import org.testfx.api.FxToolkitContext;
import org.testfx.toolkit.ApplicationLauncher;
import org.testfx.toolkit.ApplicationService;
import org.testfx.toolkit.ToolkitService;
import org.testfx.toolkit.impl.ApplicationLauncherImpl;
import org.testfx.toolkit.impl.ApplicationServiceImpl;
import org.testfx.toolkit.impl.ToolkitServiceImpl;
import org.testfx.util.WaitForAsyncUtils;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FxToolkitWrapper {

    private static final ApplicationLauncher APP_LAUNCHER = new ApplicationLauncherImpl();
    private static final ApplicationService APP_SERVICE = new ApplicationServiceImpl();
    private static final FxToolkitContext CONTEXT = new FxToolkitContext();
    private static final ToolkitServiceWrapper SERVICE;

    public static Application setupApplication(Object app, String... applicationArgs) throws TimeoutException {
        return (Application)waitForSetup(SERVICE.setupApplication(CONTEXT::getRegisteredStage, app, applicationArgs));
    }

    private static <T> T waitForSetup(Future<T> future) throws TimeoutException {
        T ret = WaitForAsyncUtils.waitFor(CONTEXT.getSetupTimeoutInMillis(), TimeUnit.MILLISECONDS, future);
        WaitForAsyncUtils.waitForFxEvents();
        return ret;
    }

    static {
        SERVICE = new ToolkitServiceWrapper(APP_LAUNCHER, APP_SERVICE);
    }

}
