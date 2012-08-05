package services;

import models.User;
import ar.uba.dc.seginf.sims.messages.PasswordExpiredMessage;

public class PasswordExpiredMessageProcessor extends TransactionalMessageProcessor<PasswordExpiredMessage> {

	@Override
	protected void doProcess(PasswordExpiredMessage msg) {
        User user = User.find("byUsername", msg.getUsername()).first();
        user.passwordExpired();
        user.save();
	}

}
