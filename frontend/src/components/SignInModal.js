import React, { useState } from 'react';
import SignUpModal from './SignUpModal';

function SignInModal({ isOpen, onClose }) {
    const [isSignUpOpen, setIsSignUpOpen] = useState(false);

    const handleBackgroundClick = (e) => {
        if (e.target === e.currentTarget) {
            onClose();
        }
    };

    const openSignUpModal = () => {
        setIsSignUpOpen(true);
        onClose();
    };

    return (
        <>
            {/* Окно авторизации */}
            {isOpen && (
                <div className="modal" onClick={handleBackgroundClick}>
                    <div className="modal-content">
                        <span className="close" onClick={onClose}>&times;</span>
                        <h2>Authorization</h2>
                        <form>
                            <label>Email</label>
                            <input type="email" name="email" placeholder="Enter your email" />

                            <label>Password</label>
                            <input type="password" name="password" placeholder="Enter your password" />

                            <button type="submit" className="signin-btn">Sign in</button>
                        </form>
                        <p className="signup-text">
                            Don't have a ONLINE BUY-SELL PLATFORM EHA account?
                            <a href="#" onClick={openSignUpModal} className="signup-link">Sign up!</a>
                        </p>
                    </div>
                </div>
            )}

            {/* Модальное окно регистрации */}
            {isSignUpOpen && <SignUpModal isOpen={isSignUpOpen} onClose={() => setIsSignUpOpen(false)} />}
        </>
    );
}

export default SignInModal;
