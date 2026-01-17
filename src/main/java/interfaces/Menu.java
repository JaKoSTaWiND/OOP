package interfaces;

import exceptions.EmptyDataException;

public interface Menu {
    void displayOptions();
    void run() throws EmptyDataException;
}
