package org.videoMaker.sql;

import org.joda.time.DateTime;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

public interface SQLogger {
    @SqlUpdate("insert into twitter (date, num_images) values (:date, :num_images)")
    void logTwitter(@Bind("date") DateTime dateTime, @Bind("num_images") int numImages);

    @SqlUpdate("insert into google (date, label) values (:date, :label)")
    void logGoogle(@Bind("date") DateTime dateTime, @Bind("label") String label);

    @SqlUpdate("insert into ffmpeg (date, url) values (:date, :url)")
    void logFfmpeg(@Bind("date") DateTime dateTime, @Bind("url") String url);
}