public class AmericanExpress extends Card {

    public AmericanExpress(String card_number) {
        super(card_number);
    }

    @Override
    String CardTypeValidation(String card_number) {
        if(card_number.equals("")){
            return "Invalid";
        }
        try {
            long number = Double.valueOf(card_number).longValue();
            String value = Long.toString(number);
            if ((value.charAt(0) == '3') && (value.charAt(1) == '4' || value.charAt(1) == '7')) {
                return "AmericanExpress";
            }
        }catch(Exception e){
            return "Invalid";
        }
        return "Invalid";
    }
}
