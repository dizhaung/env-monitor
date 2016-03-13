package org.envtools.monitor.model.querylibrary.db;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.IndexColumn;

import javax.persistence.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
/* Category --- Query OneToMany*/
/**
 * Created: 2/23/16 12:30 AM
 *
 * @author Plotnikova Anastasiya
 */
@Entity
@Table(name = "CATEGORY")
public class Category extends AbstractDbIdentifiable {

    public Category() {
    }
   // @Column(name = "CATEGORY_ID")
   // private Long id;
    private String owner; //пустое для public
    @Column(name= "TITLE")
    private String title;
    private String description;
     /*Один ко многим к таблице LibQuery*/

    @OneToMany(mappedBy = "category")
    @OrderBy(value = "TITLE")//по какой калонке будет сохраняться порядок
    private List<LibQuery> queries;


    /*Один ко многим к одной таблице*/
    @OneToMany(mappedBy = "parentCategory")
    @OrderBy(value = "TITLE")
    private List<Category> childCategories;

    @ManyToOne
    // @JoinColumn(name="CATEGORY_ID")
    private Category parentCategory;

    public Category getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }

    public List<Category> getChildCategories() {
        return childCategories;
    }

    public void setChildCategories(List<Category> childCategories) {
        this.childCategories = childCategories;
    }

    public List<LibQuery> getQueries() {
        return queries;
    }

    public void setQueries(List<LibQuery> queries) {
        this.queries = queries;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("owner", owner)
                .append("title", title)
                .append("description", description)
                .append("queries", queries)
                .append("childCategories", childCategories)
                .append("parentCategory", parentCategory)
                .toString();
    }
}
