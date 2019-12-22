package hello;

import static java.lang.String.format;

import java.util.Date;
import java.util.List;
import java.util.Map;

public final class Configuration {
    private Date released;
    private String version;
    private Connection connection;
    private List< String > protocols;
    private Map< String, String > users;

    public Date getReleased() {
        return released;
    }

    public String getVersion() {
        return version;
    }

    public void setReleased(Date released) {
        this.released = released;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public List< String > getProtocols() {
        return protocols;
    }

    public void setProtocols(List< String > protocols) {
        this.protocols = protocols;
    }

    public Map< String, String > getUsers() {
        return users;
    }

    public void setUsers(Map< String, String > users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append( format( "Version: %s\n", version ) )
                .append( format( "Released: %s\n", released ) )
                .append( format( "Connecting to database: %s\n", connection ) )
                .append( format( "Supported protocols: %s\n", protocols ) )
                .append( format( "Users: %s\n", users ) )
                .toString();
    }

    public final class Connection {
        private String url;
        private int poolSize;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public int getPoolSize() {
            return poolSize;
        }

        public void setPoolSize(int poolSize) {
            this.poolSize = poolSize;
        }

        @Override
        public String toString() {
            return String.format( "'%s' with pool of %d", getUrl(), getPoolSize() );
        }
    }


}