package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

import java.util.Optional;
import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your
 * controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use
 * the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations.
 * You should
 * refer to prior mini-project labs and lecture materials for guidance on how a
 * controller may be built.
 */

@RestController
public class SocialMediaController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private MessageService messageService;

    // 1. register
    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Account acc) {

        if (acc.getUsername().isBlank() || acc.getPassword().length() < 4) {
            return ResponseEntity.status(400).build();
        }

        if (accountService.existsByUsername(acc.getUsername())) {
            return ResponseEntity.status(409).build();
        }

        return ResponseEntity.ok(accountService.register(acc));
    }

    // 2. login
    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account acc) {

        Optional<Account> found = accountService.findByUsernameAndPassword(acc.getUsername(), acc.getPassword());

        if (!found.isPresent()) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(found.get());
    }

    // 3. message creation
    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message msg) {

        if (msg.getMessageText().isBlank() || msg.getMessageText().length() > 255) {
            return ResponseEntity.status(400).build();
        }

        if (msg.getPostedBy() == null || !accountService.findById(msg.getPostedBy()).isPresent()) {
            return ResponseEntity.status(400).build();
        }

        return ResponseEntity.ok(messageService.createMessage(msg));
    }

    // 4. retrive all messages
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        return ResponseEntity.ok(messageService.getAllMessages());
    }

    // 5. retrieve message by id
    @GetMapping("/messages/{messageId}")
    public ResponseEntity<?> getMessageById(@PathVariable Integer messageId) {
        Optional<Message> msg = messageService.getMessageById(messageId);
        return msg.isPresent() ? ResponseEntity.ok(msg.get()) : ResponseEntity.ok("");
    }

    // 6. delete message by Id
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<String> delete(@PathVariable Integer messageId) {
        int deleted = messageService.deleteMessageById(messageId);

        if (deleted == 1) {
            return ResponseEntity.ok("1");
        }

        return ResponseEntity.ok("");
    }

    // 7. update message text by id
    @PatchMapping("/messages/{messageId}")
    public ResponseEntity<String> update(@PathVariable Integer messageId, @RequestBody Message newMsg) {
        String newText = newMsg.getMessageText();

        if ( newText.isBlank() || newText.length() >= 255) {
            return ResponseEntity.status(400).body("");
        }

        if (messageService.getMessageById(messageId).isEmpty()) {
            return ResponseEntity.status(400).body("");
        }

        int updated = messageService.updateMessage(messageId, newText);
        if (updated == 1) {
            return ResponseEntity.ok("1");
        }
        return ResponseEntity.status(400).body("");
    }

    // 8. retrieve all msg written by particular user or account
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessageByAccountId(@PathVariable Integer accountId) {
        return ResponseEntity.ok(messageService.getMessageByAccountId(accountId));
    }

}
