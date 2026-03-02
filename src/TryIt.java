import com.example.tickets.IncidentTicket;
import com.example.tickets.TicketService;

import java.util.List;

// demo - shows immutability after refactor
public class TryIt {

    public static void main(String[] args) {
        TicketService service = new TicketService();

        // create ticket via service (uses Builder internally)
        IncidentTicket original = service.createTicket(
                "TCK-1001", "reporter@example.com", "Payment failing on checkout");
        System.out.println("Created:  " + original);

        // assign - returns NEW ticket, original unchanged
        IncidentTicket assigned = service.assign(original, "agent@example.com");
        System.out.println("\nAssigned: " + assigned);
        System.out.println("Original unchanged: " + original);
        System.out.println("Same object? " + (original == assigned));

        // escalate - returns NEW ticket
        IncidentTicket escalated = service.escalateToCritical(assigned);
        System.out.println("\nEscalated: " + escalated);
        System.out.println("Assigned unchanged: " + assigned);

        // try to mutate tags from outside - should throw
        List<String> tags = escalated.getTags();
        System.out.println("\nTags: " + tags);
        try {
            tags.add("HACKED_FROM_OUTSIDE");
            System.out.println("BUG: tag mutation succeeded!");
        } catch (UnsupportedOperationException e) {
            System.out.println("Tags are unmodifiable - mutation blocked (" + e.getClass().getSimpleName() + ")");
        }
        System.out.println("Tags after attack attempt: " + escalated.getTags());

        // full builder usage with all options
        IncidentTicket fancy = IncidentTicket.builder()
                .id("TCK-2002")
                .reporterEmail("user@corp.io")
                .title("Dashboard not loading")
                .description("After latest deploy, dashboard returns 500")
                .priority("HIGH")
                .tags(List.of("DASHBOARD", "P1"))
                .assigneeEmail("oncall@corp.io")
                .customerVisible(true)
                .slaMinutes(60)
                .source("WEBHOOK")
                .build();
        System.out.println("\nFull ticket: " + fancy);
    }
}
