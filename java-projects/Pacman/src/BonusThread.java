public class BonusThread extends Thread{
    private int b;

    @Override
    public void run() {
        while(!Thread.interrupted()) {
            if (Game.bonusesOnBoard.size() > 0) {
                b = Game.bonusesOnBoard.get(0);
                switch (b) {
                    case 1 -> {
                        Game.bonusesOnBoard.remove(0);
                        Game.scoreX = 2;
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        Game.scoreX = 1;
                    }
                    case 2 -> {
                        Game.bonusesOnBoard.remove(0);
                        Game.pacSpeedX = 2;
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        Game.pacSpeedX = 1;
                    }
                    case 3 -> {
                        Game.bonusesOnBoard.remove(0);
                        Game.ghostSpeedX = 2;
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        Game.ghostSpeedX = 1;
                    }
                    case 4 -> {
                        Game.bonusesOnBoard.remove(0);
                        Game.plusLiveBonus();
                    }
                    case 5 -> {
                        Game.bonusesOnBoard.remove(0);
                        Game.plusScore = true;
                    }

                }

            }
        }
    }
}
