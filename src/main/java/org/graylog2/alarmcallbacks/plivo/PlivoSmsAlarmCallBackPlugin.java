package org.graylog2.alarmcallbacks.plivo;

import org.graylog2.plugin.Plugin;
import org.graylog2.plugin.PluginMetaData;
import org.graylog2.plugin.PluginModule;

import java.util.Collection;
import java.util.Collections;


public class PlivoSmsAlarmCallBackPlugin implements Plugin {
    @Override
    public PluginMetaData metadata() {
        return new PlivoSmsAlarmCallBackMetaData();
    }

    @Override
    public Collection<PluginModule> modules () {
        return Collections.<PluginModule>singleton(new PlivoSmsAlarmCallBackModule());
    }
}
