package com.lorenzo.clientscrud.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lorenzo.clientscrud.entities.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client,Long>{
	
}
