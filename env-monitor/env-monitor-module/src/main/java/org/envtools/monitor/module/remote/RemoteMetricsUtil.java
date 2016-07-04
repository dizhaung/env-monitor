package org.envtools.monitor.module.remote;

import com.jcraft.jsch.JSchException;
import org.envtools.monitor.common.ssh.SshHelperService;
import org.envtools.monitor.model.applications.ApplicationStatus;
import org.envtools.monitor.module.configurable.VersionedApplication;
import org.envtools.monitor.module.configurable.applicationsMetadata.LinkBasedVersionLookup;
import org.envtools.monitor.module.configurable.applicationsMetadata.TagBasedProcessLookup;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Michal Skuza on 27/06/16.
 */
public class RemoteMetricsUtil {
    private SshHelperService sshHelperService;

    public RemoteMetricsUtil(SshHelperService sshHelperService) {
        this.sshHelperService = sshHelperService;
    }

    public Optional<ApplicationStatus> getProcessStatus(VersionedApplication application, TagBasedProcessLookup tagBasedProcessLookup) {
        StringBuilder cmd = new StringBuilder();

        appendProcessLookup(cmd, tagBasedProcessLookup);
        cmd.append("| wc -l");

        String result = executeCommand(application, cmd.toString());

        if (result.isEmpty()) {
            return Optional.empty();
        }

        Optional<Integer> nOccurrences = extractInt(result);

        //TODO must be equal to 1, actually
        if (nOccurrences.isPresent() && nOccurrences.get() > 0) {
            return Optional.of(ApplicationStatus.RUNNING);
        }
        return Optional.of(ApplicationStatus.STOPPED);
    }

    public Optional<String> getApplicationVersion(VersionedApplication application, LinkBasedVersionLookup versionLookup) {
        String cmd = String.format("ls -la %s/current | awk -F \"/\" '{ print $(NF-1); }'", versionLookup.getLink());
        String result = executeCommand(application, cmd);

        if (result.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(result);
        }
    }

    public Optional<Double> getProcessMemoryInMb(VersionedApplication application, TagBasedProcessLookup tagBasedProcessLookup) {
        StringBuilder cmd = new StringBuilder();

        appendProcessLookup(cmd, tagBasedProcessLookup);

        //Get memory by pid
        cmd.append("| awk '{ print $2; }' | xargs pmap | grep total | awk '{print $2; }'");

        String result = executeCommand(application, cmd.toString());

        if (result.isEmpty()) {
            return Optional.empty();
        }

        Optional<Integer> intMemoryInKb = extractInt(result);
        if (intMemoryInKb.isPresent()) {
            return Optional.of(intMemoryInKb.get() / 1000.0);
        }

        return Optional.empty();
    }

    private String executeCommand(VersionedApplication application, String cmd) {
        try {
            return sshHelperService.getHelper(application.getHost()).cmd(cmd);
        } catch (JSchException e) {
            throw new RuntimeException(e);
        }
    }

    private void appendProcessLookup(StringBuilder cmd, TagBasedProcessLookup tagBasedProcessLookup) {
        cmd.append("ps -ef ");

        for (String tag : tagBasedProcessLookup.getIncludeTags()) {
            if (tag.startsWith("!")) {
                cmd.append("| grep -v ").append("'" + tag.substring(1) + "'");
            } else {
                cmd.append("| grep ").append("'" + tag + "'");
            }
        }

        //TODO: the statement below is a thin ice, how to make it better?
        cmd.append("| grep -v grep");
    }

    private Optional<Integer> extractInt(String str) {
        Matcher matcher = Pattern.compile("\\d+").matcher(str);

        if (!matcher.find()) {
            return Optional.empty();
        }

        return Optional.of(Integer.parseInt(matcher.group()));
    }
}
