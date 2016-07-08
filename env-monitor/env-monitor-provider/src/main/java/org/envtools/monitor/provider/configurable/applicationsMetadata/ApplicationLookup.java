package org.envtools.monitor.provider.configurable.applicationsMetadata;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created by MSkuza on 2016-06-23.
 */
@XmlRootElement

@XmlAccessorType(value = XmlAccessType.PROPERTY)
public abstract class ApplicationLookup {

    @XmlElementWrapper
    @XmlElement(name = "tag")
    public abstract List<String> getIncludeTags();

    @XmlElementWrapper
    @XmlElement(name = "tag")
    public abstract List<String> getExcludeTags();

    public abstract void includeTag(String tag);
    public abstract void excludeTag(String tag);


}
