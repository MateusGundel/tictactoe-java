import java.util.Random;
import java.util.Scanner;

public class main {
    public static void main(String[] args) {
        game();
    }

    static public void game() {
        Jogador jogador = new Jogador();
        Tabuleiro tabuleiro = new Tabuleiro();
        boolean finalJogo, contiuar = true;
        Scanner scanner = new Scanner(System.in);
        int resposta;
        System.out.println("Bem vindo ao Jogo da velha");
        System.out.println("Por favor escolha um computador para jogar contra, o 1 (Computador A),2 (Computador B) ou 3(Computador C)");
        resposta = scanner.nextInt();
        Computador computador;

        if (resposta == 2) {
            computador = new ComputadorB();
        } else if (resposta == 3) {
            computador = new ComputadorC();
        } else {
            computador = new Computador();
        }


        while (contiuar) {
            jogador.jogar(tabuleiro);
            finalJogo = tabuleiro.isFinalDeJogo();
            if (!finalJogo) {
                computador.jogar(tabuleiro);
                finalJogo = tabuleiro.isFinalDeJogo();
            }

            if (finalJogo) {
                contiuar = jogarNovamente();
                if (contiuar) {
                    tabuleiro.zerarTabuleiro();
                }
            }
        }
        System.out.println("Assim ficou o tabuleiro");
        tabuleiro.mostrarTabuleiro();
        System.out.println("Obrigado por jogar, espero que tenha gostado");
    }

    static public Boolean jogarNovamente() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Deseja jogar novamente? (digite 's' ou 'sim' para continuar )");
        String resposta = scanner.next();
        return resposta.toUpperCase().equals("S") || resposta.toUpperCase().equals("SIM");
    }

}

class Jogador {
    Scanner scanner;

    public Jogador() {
        scanner = new Scanner(System.in);
    }

    public void jogar(Tabuleiro tabuleiro) {
        int x, y;
        tabuleiro.mostrarTabuleiro();
        while (true) {
            System.out.println("Por favor digite a linha onde deseja jogar");
            x = scanner.nextInt() - 1;
            System.out.println("Por favor digite a coluna onde deseja jogar");
            y = scanner.nextInt() - 1;
            if ((x < 3 && x >= 0) && (y < 3 && y >= 0)) {
                Boolean sucesso = tabuleiro.inserirJogada(x, y, "X");
                if (sucesso) {
                    break;
                }
                System.out.println("Você não pode inserir nesta posição");
            } else {
                System.out.println("Voce precisa digitar uma linha e uma coluna de 1 a 3");
            }
        }

    }
}

class Computador {
    Random gerador;

    public void jogar(Tabuleiro tabuleiro) {
        int[] jogada;
        gerador = new Random();
        boolean conseguiuJogar = false;
        while (!conseguiuJogar) {
            jogada = this.encontraJogada(tabuleiro);
            conseguiuJogar = tabuleiro.inserirJogada(jogada, "O");
        }
    }

    public int[] encontraJogada(Tabuleiro tabuleiro) {
        System.out.println("Encontrando jogada para o computador A");
        int[] jogada = new int[2];
        jogada[0] = gerador.nextInt(3);
        jogada[1] = gerador.nextInt(3);
        return jogada;
    }

}

class ComputadorB extends Computador {
    public int[] encontraJogada(Tabuleiro tabuleiro) {
        System.out.println("Encontrando jogada para o computador B");
        int[] jogada = new int[2];
        int[][] posicoes = tabuleiro.getPosicoesOcupadas();
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                if (posicoes[x][y] == 0) {
                    jogada[0] = x;
                    jogada[1] = y;
                    return jogada;
                }
            }
        }
        return jogada;
    }
}

class ComputadorC extends Computador {
    public int[] encontraJogada(Tabuleiro tabuleiro) {
        System.out.println("Encontrando jogada para o computador C");
        int[] jogada = new int[2];
        int[][] posicoes = tabuleiro.getPosicoesOcupadas();
        for (int x = 2; x >= 0; x--) {
            for (int y = 2; y >= 0; y--) {
                if (posicoes[x][y] == 0) {
                    jogada[0] = x;
                    jogada[1] = y;
                    return jogada;
                }
            }
        }
        return jogada;
    }
}

class Tabuleiro {
    private String[][] tabuleiro = new String[3][3];

    public Tabuleiro() {
        zerarTabuleiro();
    }

    public void zerarTabuleiro() {
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                tabuleiro[x][y] = "_";
            }
        }
    }

    public void mostrarTabuleiro() {
        System.out.println(tabuleiro[0][0] + " | " + tabuleiro[0][1] + " | " + tabuleiro[0][2]);
        System.out.println("--+---+--");
        System.out.println(tabuleiro[1][0] + " | " + tabuleiro[1][1] + " | " + tabuleiro[1][2]);
        System.out.println("--+---+--");
        System.out.println(tabuleiro[2][0] + " | " + tabuleiro[2][1] + " | " + tabuleiro[2][2]);
    }

    public String jogoStatus() {
        String ganhador = verificaGanhador();
        if (ganhador != null) {
            if (ganhador.equals("X")) {
                return "vitoria";
            } else {
                return "derrota";
            }
        } else if (casasLivres() == 0) {
            return "empate";
        } else {
            return "inacabado";
        }
    }

    public Boolean isFinalDeJogo() {
        String status = jogoStatus();
        switch (status) {
            case "vitoria":
                mostrarTabuleiro();
                System.out.println("Você ganhou :)");
                return true;
            case "derrota":
                mostrarTabuleiro();
                System.out.println("Você perdeu :(");
                return true;
            case "empate":
                mostrarTabuleiro();
                System.out.println("O jogo empatou :/");
                return true;
        }
        return false;
    }

    private int casasLivres() {
        int casas_livres = 0;
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                if (tabuleiro[x][y].equals("_")) {
                    casas_livres++;
                }
            }
        }
        return casas_livres;
    }

    public String verificaGanhador() {
        String resultado;
        resultado = verificaString(this.tabuleiro[0][0] + this.tabuleiro[1][1] + this.tabuleiro[2][2]);
        resultado = verificaString(this.tabuleiro[0][0] + this.tabuleiro[1][1] + this.tabuleiro[2][2]);
        if (resultado != null) {
            return resultado;
        }
        resultado = verificaString(this.tabuleiro[2][0] + this.tabuleiro[1][1] + this.tabuleiro[0][2]);
        if (resultado != null) {
            return resultado;
        }
        for (int x = 0; x < 3; x++) {
            String horizontal = "";
            String vertical = "";
            for (int y = 0; y < 3; y++) {
                horizontal = horizontal + this.tabuleiro[x][y];
                vertical = vertical + this.tabuleiro[y][x];
            }

            resultado = verificaString(horizontal);
            if (resultado != null) {
                return resultado;
            }
            resultado = verificaString(vertical);
            if (resultado != null) {
                return resultado;
            }
        }
        return null;
    }

    private String verificaString(String palavra) {
        if (palavra.equals("XXX")) {
            return "X";
        } else if (palavra.equals("OOO")) {
            return "O";
        }
        return null;
    }

    public Boolean inserirJogada(int x, int y, String caracter) {
        if (this.tabuleiro[x][y] == "_") {
            this.tabuleiro[x][y] = caracter;
            return true;
        }
        return false;
    }

    public Boolean inserirJogada(int[] pos, String caracter) {
        if (this.tabuleiro[pos[0]][pos[1]] == "_") {
            this.tabuleiro[pos[0]][pos[1]] = caracter;
            return true;
        }
        return false;
    }

    public int[][] getPosicoesOcupadas() {
        int[][] posicoes = new int[3][3];
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                if (tabuleiro[x][y].equals("_")) {
                    posicoes[x][y] = 0;
                } else {
                    posicoes[x][y] = 1;
                }
            }
        }
        return posicoes;
    }
}