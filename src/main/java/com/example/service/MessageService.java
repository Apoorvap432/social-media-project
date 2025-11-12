package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    public Message createMessage(Message message){
        return messageRepository.save(message);
    }

    public List<Message> getAllMessages(){
        return messageRepository.findAll();
    }

    public Optional<Message> getMessageById(Integer Id){
        return messageRepository.findById(Id);
    }

    public int deleteMessageById(Integer id){
        if(messageRepository.existsById(id)){
            messageRepository.deleteById(id);
            return 1;
        }
        return 0;
    }

    public int updateMessage(Integer id, String newText){
        Optional<Message> optional = messageRepository.findById(id);
        if(optional.isPresent()){
            Message m = optional.get();
            m.setMessageText(newText);
            messageRepository.save(m);
            return 1;
        }
        return 0;
    }

    public List<Message> getMessageByAccountId(Integer accountId){
        return messageRepository.findByPostedBy(accountId);
    }
}
