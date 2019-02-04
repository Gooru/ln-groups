package org.gooru.groups.reports.dbhelpers.core;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;

/**
 * @author renuka
 */
public interface CoreUserDao {
	
    @Mapper(UserModelMapper.class)
	@SqlQuery("select first_name, last_name from users where id = :userId::uuid")
	UserModel fetchUser(@Bind("userId") String userId);
}
