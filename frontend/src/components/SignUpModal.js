import React from 'react';

function SignUpModal({ isOpen, onClose }) {
    if (!isOpen) return null;

    const handleBackgroundClick = (e) => {
        if (e.target === e.currentTarget) {
            onClose();
        }
    };

    return (
        <div className="modal" onClick={handleBackgroundClick}>
            <div className="modal-content">
                <span className="close" onClick={onClose}>&times;</span>
                <h2>Registration</h2>
                <form>
                    <label>Name Surname</label>
                    <input type="text" name="name" placeholder="Name and surname" />

                    <label>Email</label>
                    <input type="email" name="email" placeholder="Email" />

                    <label>Phone Number</label>
                    <input type="tel" name="phone" placeholder="Phone number" />

                    <label>Password</label>
                    <input type="password" name="password" placeholder="Password" />

                    <label>Password Confirmation</label>
                    <input type="password" name="passwordConfirm" placeholder="Confirm your password" />

                    <button type="submit" className="signup-btn">Sign up</button>
                </form>
            </div>
        </div>
    );
}

export default SignUpModal;
