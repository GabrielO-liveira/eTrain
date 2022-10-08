# eTrain
eTrain tem como objetivo desenvolver a forma do inglês oral de seus usuários, para isso ele utiliza de algumas funções:<br/>
<br>O reconhecimento STT(Speech To Text) com a biblioteca externa; 
<br>-> A validação da frase reconhecida; 
<br>-> A classificação dentro do modelo: BOM, MÉDIO ou RUIM;
<br>-> Armazena em um banco de dados SQLite as frases a serem utilizadas;
<br>-> E as frases para treino são divididas em 5 temas.<br/>

## Imagens
![ ](https://github.com/GabrielO-liveira/e-Train/blob/Main/app/src/main/assets/Inicial.png?raw=true)
![ ](https://github.com/GabrielO-liveira/e-Train/blob/Main/app/src/main/assets/Acertou.png?raw=true)
![ ](https://github.com/GabrielO-liveira/e-Train/blob/Main/app/src/main/assets/errou.png?raw=true)<br/>

# Instalação e utilização
<br>Para a utilização é necessario apenas fazer o download do arquivo .ZIP, extrai-lo em uma pasta a sua escolha e abrir em sua IDE.
<br>Com isso feito, é necessario carregar os arquivos gradle, e pronto você concluiu a instalação.<br/>

## Mudança nos dados
Para que o aplicativo não ficasse, pesado ou mutio grande optamos pela criação de um DB SQLite.
<br>Ele funciona de maneira onde ele pega o DB original e realiza uma cópia dele e insere dentro do dispositivo, porém quando é feita a mudança no DB original não é atualizado os demais.<br>
<br>Para atualizar o DB do dispositivo é preciso:
<br> 1.**Escrever as modificações no DB browser**<br/>
![image](https://user-images.githubusercontent.com/78834753/162976323-7a1795bf-255c-4947-bab4-8022df0c23d6.png)
<br> 2.**Desinstalar o APP (caso já tenha dado o RUN);**
<br> 3.**Abrir sua IDE e dar um reload from Disk;**<br/>
![image](https://user-images.githubusercontent.com/78834753/162815902-3231f4cc-0276-4b87-a74e-2e5de16a6bf4.png)
<br> 4.**E por fim na sua IDE, de o RUN para rodar o APP.**<br/>
![image](https://user-images.githubusercontent.com/78834753/162979333-79b724e0-257b-4597-b6e1-d3bd7bff73a4.png)<br/>


# Créditos
<br>O eTrain tem como biblioteca externa VOSK, uma biblioteca de código aberto que suporta 20 idiomas dentre eles o inglês.
<br>Para visualizar a documentação e obter mais informações sobre o VOSK, [clique aqui](https://alphacephei.com/vosk/).
