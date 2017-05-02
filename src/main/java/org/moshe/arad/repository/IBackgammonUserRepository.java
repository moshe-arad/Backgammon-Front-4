package org.moshe.arad.repository;

import org.moshe.arad.entities.BackgammonUserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBackgammonUserRepository extends JpaRepository<BackgammonUserDetails,Long>{

	public BackgammonUserDetails findByUserName(String UserName); 
}
