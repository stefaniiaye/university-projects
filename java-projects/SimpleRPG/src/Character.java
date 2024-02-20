import java.util.Scanner;
    public class Character {
        public int XP;
        public int HP;
        public int Stamina;
        public int Mana;
        public String name;
        public Item[] Items = new Item[100];
        public Item item;

        public Character(int hp, int mana, int stamina, String name, Item item){
            this.HP = hp;
            this.Mana = mana;
            this.Stamina = stamina;
            this.name = name;
            this.item = item;
        }
        public Character(){
        }

        public void showStats(Character character, Character[] Monsters_Char, int move){
            System.out.println("My Hp " + character.HP + " My Mana " + character.Mana + " My Stamina " + character.Stamina);
            for (int i = 0;i<100;i++){
                if(character.Items[i].name.equals("healsp")||character.Items[i].name.equals("manasp")||character.Items[i].name.equals("staminasp")){
                    System.out.println("Your spell is " + character.Items[i].name);
                }
            }
            System.out.println();
            System.out.println("His name " + Monsters_Char[move].name);
            System.out.println("His HP " + Monsters_Char[move].HP);
        }
        public Character setAllvalues(Character character){
            Scanner scan = new Scanner(System.in);
            int n = 15;
            System.out.println("Enter value of HP, " + n + " is left");
            character.HP = scan.nextInt();
            n = n-HP;
            System.out.println("Enter value of Stamina, " + n + " is left");
            character.Stamina = scan.nextInt();
            n = n-Stamina;
            character.Mana = n;
            System.out.println("Your values are: " + "HP - " + HP + " Stamina - " + Stamina + " Mana - " + Mana + " My XP " + XP);
            for(int i = 0;i<100;i++){
                character.Items[i] = new Item("00");
            }
            character.Items[0] = new Item("healsp");
            character.Items[1] = new Item("manasp");
            character.Items[2] = new Item("staminasp");
            return character;
        }
        public void attackM(Character character, int move, Character[] M){
            character.Mana --;
            M[move].HP--;
            character.HP--;
        }
        public void attackPh(Character character, int move, Character[] M){
            M[move].HP--;
            character.Stamina --;
            character.HP--;
        }
        public boolean IsCharOrMonst(Character[] Monsters_Char, int move){
            if(Monsters_Char[move].name.equals("Ally")) return true;
            else return false;
        }
        public boolean IsThereAllies(Character[] Monsters_Char, int move){
            boolean k = true;
            for(int i = 0;i<move;i++){
                if(Monsters_Char[i].IsCharOrMonst(Monsters_Char,i)){
                    k =true;break;
                }else k = false;
            }return k;
        }
        public Character AllyHelp(Character[] Monsters_Char, int move){
            int k = 0;
            for(int i = 0;i<move;i++){
                if(Monsters_Char[i].IsCharOrMonst(Monsters_Char, i) && Monsters_Char[i].HP>0) k++;
            }
            Character All = new Character();
            All.HP = k*4;
            All.Mana = k*5;
            All.Stamina = k*6;
            return All;
        }
        public void useItem(String item1){
            Item item = new Item(item1);
            if(item.name.equals("healsp")){
                for(int i = 0;i<Items.length;i++) {
                    if (Items[i].name.equals("healsp")) {
                        HP++;
                        Items[i].name = "00";
                        break;
                    }else System.out.println("There is no such type of spell");
                }
            } else if (item.name.equals("staminasp")) {
                for(int i = 0;i<Items.length;i++) {
                    if (Items[i].name.equals("staminasp")) {
                        Stamina++;
                        Items[i].name = "00";
                        break;
                    }else System.out.println("There is no such type of spell");
                }
            }else if (item.name.equals("manasp")) {
                for(int i = 0;i<Items.length;i++) {
                    if (Items[i].name.equals("manasp")) {
                        Mana++;
                        Items[i].name = "00";
                        break;
                    }else System.out.println("There is no such type of spell");
                }
            }
        }
    }

