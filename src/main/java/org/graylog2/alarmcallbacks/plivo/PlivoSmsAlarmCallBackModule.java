package org.graylog2.alarmcallbacks.plivo;

import org.graylog2.plugin.PluginModule;


public class PlivoSmsAlarmCallBackModule extends PluginModule {

    @Override
    protected void configure() {
        addAlarmCallback(PlivoSmsAlarmCallback.class);
    }
}
