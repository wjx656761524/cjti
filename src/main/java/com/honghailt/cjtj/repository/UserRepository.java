package com.honghailt.cjtj.repository;

import com.honghailt.cjtj.domain.User;
import com.honghailt.cjtj.domain.enumeration.Version;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 *
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 获取单个卖家信息
     * @param nick
     * @return
     */
    Optional<User> findOneByNick(String nick);

    List<User> findAllByNickIn(Collection<String> nicks);

    /**
     * 根据版本和sessionkey是否正常查询user
     * @param versions
     * @param sessionkeyIsInvalid
     * @return
     */
    List<User> findUsersByVersionInAndSessionkeyIsInvalid(List<Version> versions, Boolean sessionkeyIsInvalid);

}
