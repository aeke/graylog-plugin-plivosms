package org.graylog2.alarmcallbacks.plivo;

import org.graylog2.plugin.PluginMetaData;
import org.graylog2.plugin.ServerStatus;
import org.graylog2.plugin.Version;

import java.net.URI;
import java.util.Collections;
import java.util.Set;

/**
 * Implement the PluginMetaData interface here.
 */
public class PlivoSmsAlarmCallBackMetaData implements PluginMetaData {
    @Override
    public String getUniqueId() {
        return PlivoSmsAlarmCallback.class.getCanonicalName();
    }

    @Override
    public String getName() {
        return "Plivo SMS AlarmCallBack Plugin";
    }

    @Override
    public String getAuthor() {
        return "Abdullah EKE <abdheke@gmail.com>";
    }

    @Override
    public URI getURL() {
        return URI.create("https://github.com/aeke/graylog-plugin-plivo");
    }

    @Override
    public Version getVersion() {
        return new Version(1, 0, 0);
    }

    @Override
    public String getDescription() {
        return "Sends all stream alerts as SMS to a defined source phone number over Plivo";
    }

    @Override
    public Version getRequiredVersion() {
        return new Version(1, 0, 0);
    }

    @Override
    public Set<ServerStatus.Capability> getRequiredCapabilities() {
        return Collections.emptySet();
    }
}
