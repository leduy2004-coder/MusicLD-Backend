package com.myweb.MusicLD.repository;

import com.myweb.MusicLD.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, BigInteger> {

}