package org.moshe.arad;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BackgammonUserRepository extends JpaRepository<BackgammonUser,String>{

	public BackgammonUser findByUserName(String UserName);
}
