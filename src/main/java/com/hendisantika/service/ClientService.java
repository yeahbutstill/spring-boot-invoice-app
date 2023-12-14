package com.hendisantika.service;

import com.hendisantika.model.Client;
import com.hendisantika.model.Invoice;
import com.hendisantika.model.Product;
import com.hendisantika.repository.ClientRepository;
import com.hendisantika.repository.InvoiceRepository;
import com.hendisantika.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-invoice-app
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 14/09/21
 * Time: 06.51
 */
@Service
@Transactional
public class ClientService {
    private final ClientRepository clientRepository;

    private final ProductRepository productRepository;

    private final InvoiceRepository invoiceRepository;

    public ClientService(ClientRepository clientRepository, ProductRepository productRepository, InvoiceRepository invoiceRepository) {
        this.clientRepository = clientRepository;
        this.productRepository = productRepository;
        this.invoiceRepository = invoiceRepository;
    }

    public List<Client> findAll() {
        return clientRepository.findAll();
    }

    public void save(Client client) {
        clientRepository.save(client);
    }

    public Client findOne(Long id) {
        //return clientDao.findOne(id); //Spring Boot 1.5.10
        return clientRepository.findById(id).orElse(null);    //Spring Boot 2
    }

    public Client fetchByIdWithInvoice(Long id) {
        return clientRepository.fetchByIdWithInvoice(id);
    }

    public void delete(Long id) {
        //clientDao.delete(id);
        clientRepository.deleteById(id);    //Spring Boot 2
    }

    public Page<Client> findAll(Pageable page) {
        return clientRepository.findAll(page);
    }

    public List<Product> findByName(String search) {
        return productRepository.findByNameLikeIgnoreCase("%" + search + "%");
    }

    public void saveInvoice(Invoice invoice) {
        invoiceRepository.save(invoice);
    }

    public Product findProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public Invoice findInvoiceById(Long id) {
        return invoiceRepository.findById(id).orElse(null);
    }

    public void deleteInvoice(Long id) {
        invoiceRepository.deleteById(id);
    }

    public Invoice fetchByIdWithClientWithInvoiceLineWithProduct(Long id) {
        return invoiceRepository.fetchByIdWithClientWithInvoiceLineWithProduct(id);
    }

}
