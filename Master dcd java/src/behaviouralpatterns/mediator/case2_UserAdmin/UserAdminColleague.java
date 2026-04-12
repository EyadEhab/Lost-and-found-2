package behaviouralpatterns.mediator.case2_UserAdmin;

/**
 * Mediator Pattern - Colleague Interface
 *
 * Base interface for all user admin UI components (colleagues).
 * Each colleague holds a reference to the mediator and communicates
 * through it rather than directly with other colleagues.
 *
 * Role: Colleague (interface)
 */
public interface UserAdminColleague {

    /**
     * Sets the mediator reference for this colleague.
     * Called by the mediator during registration.
     *
     * @param mediator the mediator instance
     */
    void setMediator(UserAdminMediator mediator);

    /**
     * Receives notifications/events from the mediator.
     * Allows the mediator to broadcast state changes to colleagues.
     *
     * @param event the event type
     * @param data the event data
     */
    void onMediatorEvent(String event, Object data);

    /**
     * Gets the current state of this colleague.
     * For fields, this returns the current text value.
     * For dropdowns, this returns selected item.
     * For buttons, this may return enabled/disabled state.
     *
     * @return the current state
     */
    Object getState();
}
