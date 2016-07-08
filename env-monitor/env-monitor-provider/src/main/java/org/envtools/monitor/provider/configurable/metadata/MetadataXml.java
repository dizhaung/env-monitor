package org.envtools.monitor.provider.configurable.metadata;

import javax.xml.bind.annotation.*;

/**
 * Created by Michal Skuza on 2016-06-23.
 */
@XmlRootElement(name = "metadata")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class MetadataXml {
    @XmlElements({@XmlElement(name = "tagBasedProcessLookup", type = TagBasedProcessLookupXml.class)})
    private ApplicationLookupXml applicationLookupXml;


    @XmlElements({@XmlElement(name = "linkBasedVersionLookup", type = LinkBasedVersionLookupXml.class)})
    private VersionLookupXml versionLookup;

    public MetadataXml() {
    }

    public MetadataXml(ApplicationLookupXml applicationLookupXml) {
        this.applicationLookupXml = applicationLookupXml;
    }

    public ApplicationLookupXml getApplicationLookupXml() {
        return applicationLookupXml;
    }

    public void setApplicationLookupXml(ApplicationLookupXml applicationLookupXml) {
        this.applicationLookupXml = applicationLookupXml;
    }

    public VersionLookupXml getVersionLookup() {
        return versionLookup;
    }

    public void setVersionLookup(VersionLookupXml versionLookupXml) {
        this.versionLookup = versionLookupXml;
    }
}
