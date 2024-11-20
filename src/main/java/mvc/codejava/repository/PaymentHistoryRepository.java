/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mvc.codejava.repository;


import mvc.codejava.entity.PaymentHistoryEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentHistoryRepository extends CrudRepository<PaymentHistoryEntity, Long> {
}
