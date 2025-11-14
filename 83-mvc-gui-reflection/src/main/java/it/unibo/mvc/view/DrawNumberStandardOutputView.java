package it.unibo.mvc.view;

import it.unibo.mvc.api.DrawNumberController;
import it.unibo.mvc.api.DrawNumberView;
import it.unibo.mvc.api.DrawResult;

/**
 * Una vista "solo output" che scrive sul terminale (stdout).
 */
public final class DrawNumberStandardOutputView implements DrawNumberView {

    @Override
    public void setController(final DrawNumberController observer) {
        // This view is output-only, it does not interact with the controller.
        // Empty method
    }

    @Override
    public void start() {
        // This view is non-interactive, it doesn't need to be "started".
        // Empty method
    }

    @Override
    public void result(final DrawResult res) {
        switch (res) {
            case YOURS_HIGH:
            case YOURS_LOW:
            case YOU_WON:
            case YOU_LOST:
                System.out.println(res.getDescription()); //NOPMD
                break;
        }
    }
}
