// src/context/LocalizationContext.js
import React, { createContext, useState, useEffect } from 'react';
import axios from 'axios';

export const LocalizationContext = createContext();

export const LocalizationProvider = ({ children }) => {
    const [messages, setMessages] = useState({});
    const [language, setLanguage] = useState('en');

    const fetchMessages = async (lang) => {
        try {
            const response = await axios.get(`/api/localization/messages?lang=${lang}`);
            setMessages(response.data);
        } catch (error) {
            console.error('Error fetching localized messages:', error);
        }
    };

    useEffect(() => {
        fetchMessages(language);
    }, [language]);

    return (
        <LocalizationContext.Provider value={{ messages, language, setLanguage }}>
            {children}
        </LocalizationContext.Provider>
    );
};
