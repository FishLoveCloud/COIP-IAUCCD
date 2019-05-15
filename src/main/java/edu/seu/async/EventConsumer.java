package edu.seu.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class EventConsumer implements InitializingBean, ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);
    // 将EventType和EventHandler对应起来
    private Map<EventType, EventHandler> config = new HashMap<>();
    private ApplicationContext applicationContext;

    @Autowired
    ThreadPoolTaskExecutor taskExecutor;

    @Override
    public void afterPropertiesSet() throws Exception {

    	Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        if (beans != null) {
            for (Map.Entry<String, EventHandler> entry : beans.entrySet()) {
                EventType eventType = entry.getValue().belongTo();
                config.put(eventType, entry.getValue());
            }
        }

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void submit(EventModel eventModel) {
        EventHandler eventHandler = config.get(eventModel.getEventType());
        EventTask eventTask = new EventTask(eventHandler, eventModel);
        taskExecutor.submit(eventTask);
    }
    
}
