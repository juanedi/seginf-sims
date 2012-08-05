package services;

import play.db.jpa.GenericModel.JPAQuery;
import models.User;
import ar.uba.dc.seginf.sims.messages.UserRemovedMessage;

public class UserRemovedMessageProcessor extends TransactionalMessageProcessor<UserRemovedMessage> {

	
	@Override
	protected void doProcess(UserRemovedMessage msg) {
		User user = User.find("byUsername", msg.getUsername()).first();
		user.delete();
	}

}
