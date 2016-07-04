package org.envtools.monitor.module.configurable.applicationsMetadata;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by MSkuza on 2016-06-23.
 */
@XmlRootElement(name = "metadata")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class Metadata {
    //    @XmlElement
    @XmlElements({@XmlElement(name = "tagBasedProcessLookup", type = TagBasedProcessLookup.class)})
    private ApplicationLookup applicationLookup;

    //    @XmlElement
//@XmlElementRef
    @XmlElements({@XmlElement(name = "linkBasedVersionLookup", type = LinkBasedVersionLookup.class)})
    private VersionLookup versionLookup;

    public Metadata() {
    }

    public Metadata(ApplicationLookup applicationLookup) {
        this.applicationLookup = applicationLookup;
    }

    public ApplicationLookup getApplicationLookup() {
        return applicationLookup;
    }

    public void setApplicationLookup(ApplicationLookup applicationLookup) {
        this.applicationLookup = applicationLookup;
    }

    public VersionLookup getVersionLookup() {
        return versionLookup;
    }

    public void setVersionLookup(VersionLookup versionLookup) {
        this.versionLookup = versionLookup;
    }
}
