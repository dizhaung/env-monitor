package org.envtools.monitor.provider.configurable.metadata;

/**
 * Created by Michal Skuza on 2016-06-23.
 */

public class LinkBasedVersionLookupXml extends VersionLookupXml {
    public String link;
    public String linkTargetPattern;

    public LinkBasedVersionLookupXml() {
    }

    public LinkBasedVersionLookupXml(String link, String linkTargetPattern) {
        this.link = link;
        this.linkTargetPattern = linkTargetPattern;
    }

    @Override
    public String getLink() {
        return link;
    }

    @Override
    public String getLinkTargetPattern() {
        return linkTargetPattern;
    }
}
