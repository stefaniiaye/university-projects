import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Character character = new Character();
        character.setAllvalues(character);

        Scanner scan = new Scanner(System.in);

        Character[] Monsters_Char = new Character[100];
        for(int i = 0;i<100;i++){
            double k = Math.random()*3;
            if (k <= 1) Monsters_Char[i] = new Character(3, 0, 5, "Ally",new Item(Double.toString(Math.random()*3 )));
            else if (k > 1 && k <= 3) Monsters_Char[i] = new Monster(3, 1, "Monster",new Item(Double.toString(Math.random()*3 )));
        }
        int move = 0;

        do{
            System.out.println("Your stats for move " + (move+1));
            character.showStats(character,Monsters_Char,move);
            if(Monsters_Char[move].IsCharOrMonst(Monsters_Char,move)){
                System.out.println("They are allies, you can call them for help ");
            }
            System.out.println("What do you want to do?");
            String action = scan.nextLine();
            switch (action) {
                case ("attackm"):
                    do {
                        character.attackM(character, move, Monsters_Char);
                    } while (character.HP > 0 && Monsters_Char[move].HP > 0 && character.Mana>0);
                    if(Monsters_Char[move].HP==0){
                        if(Double.parseDouble(Monsters_Char[move].item.name)>=0 && Double.parseDouble(Monsters_Char[move].item.name)<=1){
                            Monsters_Char[move].item.name = "healsp";
                        }else if(Double.parseDouble(Monsters_Char[move].item.name)>1 && Double.parseDouble(Monsters_Char[move].item.name)<=2){
                            Monsters_Char[move].item.name = "staminasp";
                        }else if(Double.parseDouble(Monsters_Char[move].item.name)>2 && Double.parseDouble(Monsters_Char[move].item.name)<=3){
                            Monsters_Char[move].item.name = "manasp";
                        }
                        character.Items[move+3] = Monsters_Char[move].item;
                    }
                    character.showStats(character, Monsters_Char, move);
                    if(Monsters_Char[move].HP<=0){
                        move++;}
                    break;
                case ("attackph"):
                    do {
                        character.attackPh(character, move, Monsters_Char);
                    } while (character.HP > 0 && Monsters_Char[move].HP > 0&&character.Stamina>0);
                    if(Monsters_Char[move].HP==0){
                        if(Double.parseDouble(Monsters_Char[move].item.name)>=0 && Double.parseDouble(Monsters_Char[move].item.name)<=1){
                            Monsters_Char[move].item.name = "healsp";
                        }else if(Double.parseDouble(Monsters_Char[move].item.name)>1 && Double.parseDouble(Monsters_Char[move].item.name)<=2){
                            Monsters_Char[move].item.name = "staminasp";
                        }else if(Double.parseDouble(Monsters_Char[move].item.name)>2 && Double.parseDouble(Monsters_Char[move].item.name)<=3){
                            Monsters_Char[move].item.name = "manasp";
                        }
                        character.Items[move+3] = Monsters_Char[move].item;
                    }
                    character.showStats(character, Monsters_Char, move);
                    if(Monsters_Char[move].HP<=0){
                        move++;}
                    break;
                case ("useItem"):
                    String item;
                    System.out.println("Choose what item do you want to use");
                    item = scan.nextLine();
                    character.useItem(item);
                    break;
                case ("nothing"):
                    move++;
                    break;
                case ("allyhelp"):
                    if(!character.IsThereAllies(Monsters_Char,move)){
                        System.out.println("There are no allies");
                        break;
                    }
                    do {
                        character.attackPh(character.AllyHelp(Monsters_Char, move), move, Monsters_Char);
                    } while (character.AllyHelp(Monsters_Char, move).HP > 0 && Monsters_Char[move].HP > 0 &&character.AllyHelp(Monsters_Char, move).Stamina > 0);
                    if(Monsters_Char[move].HP==0){
                        if(Double.parseDouble(Monsters_Char[move].item.name)>=0 && Double.parseDouble(Monsters_Char[move].item.name)<=1){
                            Monsters_Char[move].item.name = "healsp";
                        }else if(Double.parseDouble(Monsters_Char[move].item.name)>1 && Double.parseDouble(Monsters_Char[move].item.name)<=2){
                            Monsters_Char[move].item.name = "staminasp";
                        }else if(Double.parseDouble(Monsters_Char[move].item.name)>2 && Double.parseDouble(Monsters_Char[move].item.name)<=3){
                            Monsters_Char[move].item.name = "manasp";
                        }
                        character.Items[move+3] = Monsters_Char[move].item;
                        move++;
                    }
                default:
                    System.out.println("Wrong command");
                    break;
            }
            if(!Monsters_Char[move].IsCharOrMonst(Monsters_Char,move) && Monsters_Char[move].HP<=0){
                character.XP++;
            }
            if(character.XP%5==0&&character.XP!=0){
                System.out.println("Choose which attribute do you want to increase by five");
                String s = scan.nextLine();
                switch (s){
                    case ("HP"):character.HP+=5;
                        break;
                    case ("Mana"):character.Mana+=5;
                        break;
                    case ("Stamina"):character.Stamina+=5;
                        break;
                }
            }
            if(character.HP <= 0){
                System.out.println("Game over");
                move=101;
            }
        }while (character.HP>0 || move<100);
    }
}


