public class Monster extends Character{
    public int ExpToGive;
    public Monster(int hp, int expToGive, String name,Item item){
        this.HP = hp;
        this.ExpToGive = expToGive;
        this.name = name;
        this.item = item;
    }
}
