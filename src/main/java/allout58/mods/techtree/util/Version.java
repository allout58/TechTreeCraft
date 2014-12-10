package allout58.mods.techtree.util;

/**
 * Created by James Hollowell on 12/5/2014.
 */
public class Version implements Comparable<Version>
{
    private int major = 0;
    private int minor = 0;
    private int build = 0;
    private int revision = 0;

    public Version(int major, int minor, int build, int revision)
    {
        this.major = major;
        this.minor = minor;
        this.build = build;
        this.revision = revision;
    }

    public int getMajor()
    {
        return major;
    }

    public int getMinor()
    {
        return minor;
    }

    public int getBuild()
    {
        return build;
    }

    public int getRevision()
    {
        return revision;
    }

    @Override
    public String toString()
    {
        return major + "." + minor + "." + build + (revision != 0 ? "-rev" + revision : "");
    }

    public void readFromString(String s)
    {
        String pre = s.contains("-rev") ? s.split("-rev")[0] : s;
        major = Integer.parseInt(pre.split("\\.")[0]);
        minor = Integer.parseInt(pre.split("\\.")[1]);
        build = Integer.parseInt(pre.split("\\.")[2]);
        if (s.contains("-rev"))
        {
            revision = Integer.parseInt(s.split("-rev")[1]);
        }
    }

    @Override
    public boolean equals(Object o)
    {
        if (!(o instanceof Version)) return false;
        Version other = (Version) o;
        return this.compareTo(other) == 0;
    }

    @Override
    public int compareTo(Version o)
    {
        final int before = -1;
        final int equal = 0;
        final int after = 1;
        if (this == o) return equal;

        if (this.major < o.major) return before;
        if (this.major > o.major) return after;

        if (this.minor < o.minor) return before;
        if (this.minor > o.minor) return after;

        if (this.build < o.build) return before;
        if (this.build > o.build) return after;

        if (this.revision < o.revision) return before;
        if (this.revision > o.revision) return after;

        return equal;
    }
}
