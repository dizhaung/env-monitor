package org.envtools.monitor.model.querylibrary.db;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import javax.persistence.*;
import java.util.Set;

/**
 * Created: 2/23/16 12:30 AM
 *
 * @author Yury Yakovlev
 */
@Entity
public class QueryExecution extends AbstractDbIdentifiable {

    public QueryExecution() {
    }
    @Column(name = "QUERYEXECUTION_ID")
    private Long id;
    private String user;
    private Long start_timestamp;
    private Long end_timestamp;

    @ManyToOne
    @JoinColumn(name="QUERY_ID")
    private Query query;

    @OneToMany(mappedBy = "queryExecution")
    private Set<QueryExecutionParam> queryExecutionParam;

    @OneToOne(mappedBy = "queryExecution")
    private DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Set<QueryExecutionParam> getQueryExecutionParam() {
        return queryExecutionParam;
    }

    public void setQueryExecutionParam(Set<QueryExecutionParam> queryExecutionParam) {
        this.queryExecutionParam = queryExecutionParam;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public Long getStart_timestamp() {
        return start_timestamp;
    }

    public void setStart_timestamp(Long start_timestamp) {
        this.start_timestamp = start_timestamp;
    }

    public Long getEnd_timestamp() {
        return end_timestamp;
    }

    public void setEnd_timestamp(Long end_timestamp) {
        this.end_timestamp = end_timestamp;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("user", user).
                append("start_timestamp",  start_timestamp.toString()).
                        append("end_timestamp",  end_timestamp.toString()).
                toString();
    }
}
