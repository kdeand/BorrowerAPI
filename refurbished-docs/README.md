Why the refurbish?

The BORROWER API that I am currently building is experiencing some hiccups with development, and/or testing. 
There are various things that the developer does not know yet and that is not the correct way of building something.
So to remedy the issue, BORROWER API will undergo a refurbish-- and yes, there is the option to restart the whole thing 
but if I'm going to be honest, abandoning the project would only prove that I am a loser and that I am nothing but a  quitter.
I am done being a quitter. 

And thus, the recondition commences. 

```
List<BorrowRequestItem> items =
borrowRequestItemRepository.findByBorrowRequestId(id);

for (BorrowRequestItem item : items) {

    Equipment equipment = item.getEquipment();

    if (equipment.getStatus() != EquipmentStatus.AVAILABLE) {
        return null;
    }
}```